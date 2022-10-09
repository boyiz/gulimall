package com.boyiz.gulimall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.shaded.org.checkerframework.checker.units.qual.A;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boyiz.common.utils.PageUtils;
import com.boyiz.common.utils.Query;
import com.boyiz.gulimall.product.dao.CategoryDao;
import com.boyiz.gulimall.product.entity.CategoryEntity;
import com.boyiz.gulimall.product.service.CategoryBrandRelationService;
import com.boyiz.gulimall.product.service.CategoryService;
import com.boyiz.gulimall.product.vo.Catelog2Vo;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    RedissonClient redisson;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<CategoryEntity> listWithTree() {
        //1、查询所有分类
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        //2、组装父子树形结构
        //(1、找到所有一级分类
        List<CategoryEntity> collect = categoryEntities.stream().filter(
                categoryEntity -> categoryEntity.getParentCid() == 0
        ).peek((menu) -> menu.setChildren(getChildrens(menu, categoryEntities))).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());

        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //删除之前先判断
        //TODO  检查当前要删除的菜单是否被引用
        baseMapper.deleteBatchIds(asList);

    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);

        //将path逆序转换
        Collections.reverse(parentPath);
        return (Long[]) parentPath.toArray(new Long[0]);
    }

    /**
     * 级联更新所有关联数据
     *
     * @param category
     * @CacheEvict:失效模式
     * @Caching:同时进行多种缓存操作
     * @CacheEvict (value = "XXX",allEntries = true ):删除某个分区下的所有数据，失效模式
     * @同一类型的数据可以指定为同一个分区
     * @CachePut:双写模式
     */

//    @Caching(evict = {
//            @CacheEvict(value = "category", key = "'getLevel1Categorys'"),
//            @CacheEvict(value = "category", key = "'getCatalogJson'")
//    })
    //@CacheEvict(value = "category", key = "'getLevel1Categorys'")
    @CacheEvict(value = "category",allEntries = true ) //删除category分区中所有数据
    @Transactional
    @Override
    public void updateCascade(CategoryEntity category) {
        this.updateById(category);
        categoryBrandRelationService.updateCascade(category.getCatId(), category.getName());

    }





    //每个缓存数据都需要指定放入哪个名字的缓存中【缓存的分区，按照业务类型分】
    //         value:缓存分区名，        key:缓存数据的键,字符串加上',ex:key = "'level1Categorys'"  ,设置方法名:key = "#root.method.name
    @Cacheable(value = {"category"}, key = "#root.method.name") //代表当前方法的结果需要缓存，如果缓存中有，方法不调用，没有则调用并将结果放入缓存
    @Override
    public List<CategoryEntity> getLevel1Categorys() {
        return baseMapper.selectList(new QueryWrapper<CategoryEntity>().eq("parent_cid", 0));
        //return null;
    }

    @Cacheable(value = {"category"}, key = "#root.methodName")
    @Override
    public Map<String, List<Catelog2Vo>> getCatalogJson() {
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);
        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream()
                .collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    //1、每一个的一级分类,查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());
                    //2、封装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;
                    if (categoryEntities != null) {
                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());
                            //1、找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());
                            if (level3Catelog != null) {
                                List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                                    //2、封装成指定格式
                                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());
                                    return category3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(category3Vos);
                            }
                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        return parentCid;
    }

    //加入Redis缓存

    public Map<String, List<Catelog2Vo>> getCatalogJson2() {
        /**
         * 完善：
         *  1、空结果缓存，缓解缓存穿透
         *  2、设置过期时间（加随机值）解决缓存雪崩
         *  3、加锁，解决缓存击穿
         */

        //查询redis中是否存在
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        //如果不存在
        if (StringUtils.isEmpty(catalogJSON)) {
            //从数据库获取
            Map<String, List<Catelog2Vo>> catalogJsonFromDB = getCatalogJsonFromDBWithRedisLock();

        }
        //redis中存在，将String反序列化
        Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
        });
        return result;
    }

    //使用redisson
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithRedissonLock() {
        //获取分布式锁
        RLock lock = redisson.getLock("catalogJSON-lock");
        lock.lock();
        Map<String, List<Catelog2Vo>> dataFromDB;
        try {
            dataFromDB = getDataFromDB();
        } finally {
            lock.unlock();
        }
        return dataFromDB;
    }

    //从数据库查询并封装数据
    //redis 锁
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithRedisLock() {
        //1、占分布式锁，去redis占坑
        String uuid = UUID.randomUUID().toString();
        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent("lock", uuid, 300, TimeUnit.SECONDS);
        if (lock) {
            System.out.println("获取分布式锁成功==");
            //加锁成功，执行业务
            //2、设置过期时间，必须和加锁是同步的，原子的
//            stringRedisTemplate.expire("lock", );
            //Map<String, List<Catelog2Vo>> dataFromDB = getDataFromDB();
            //删除锁
//            stringRedisTemplate.delete("lock");
            //（获取值对比，对比成功再删除）<-- 原子操作 lua脚本操作
//            String lockValue = stringRedisTemplate.opsForValue().get("lock");
//            if (uuid.equals(lockValue)) {
//                stringRedisTemplate.delete("lock");
//            }
            //删除锁,lua脚本保证原子操作
            ///String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            //Integer lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Integer>(script, Integer.class), Arrays.asList("lock"), uuid);
            Map<String, List<Catelog2Vo>> dataFromDB;
            try {
                dataFromDB = getDataFromDB();
            } finally {
                String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
                Long lock1 = stringRedisTemplate.execute(new DefaultRedisScript<Long>(script, Long.class), Arrays.asList("lock"), uuid);
            }
            return dataFromDB;

        } else {
            //加锁失败。。。等待重试
            System.out.println("获取分布式锁失败，等待重试==");
            return getCatalogJsonFromDBWithRedisLock(); //自旋
        }
    }

    private Map<String, List<Catelog2Vo>> getDataFromDB() {
        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            //缓存不为null直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }

        System.out.println("查询了数据库");

        //将数据库的多次查询变为一次
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream()
                .collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    //1、每一个的一级分类,查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

                    //2、封装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;
                    if (categoryEntities != null) {
                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                            //1、找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                            if (level3Catelog != null) {
                                List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                                    //2、封装成指定格式
                                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                                    return category3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(category3Vos);
                            }

                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        //将数据放入缓存
        stringRedisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(parentCid), 1, TimeUnit.DAYS);

        return parentCid;
    }

    //从数据库查询并封装数据
    //本地锁，synchronized
    public Map<String, List<Catelog2Vo>> getCatalogJsonFromDBWithLocalLock() {

        String catalogJSON = stringRedisTemplate.opsForValue().get("catalogJSON");
        if (!StringUtils.isEmpty(catalogJSON)) {
            //缓存不为null直接返回
            Map<String, List<Catelog2Vo>> result = JSON.parseObject(catalogJSON, new TypeReference<Map<String, List<Catelog2Vo>>>() {
            });
            return result;
        }

        System.out.println("查询了数据库");

        //只要是同一把锁，就能锁住这个锁的所有线程
        //1、synchronized (this)：SpringBoot所有的组件在容器中都是单例的。
        //TODO 本地锁：synchronized，JUC（Lock),在分布式情况下，想要锁住所有，必须使用分布式锁

        //将数据库的多次查询变为一次
        List<CategoryEntity> selectList = this.baseMapper.selectList(null);

        //1、查出所有分类
        //1、1）查出所有一级分类
        List<CategoryEntity> level1Categorys = getParent_cid(selectList, 0L);

        //封装数据
        Map<String, List<Catelog2Vo>> parentCid = level1Categorys.stream()
                .collect(Collectors.toMap(k -> k.getCatId().toString(), v -> {
                    //1、每一个的一级分类,查到这个一级分类的二级分类
                    List<CategoryEntity> categoryEntities = getParent_cid(selectList, v.getCatId());

                    //2、封装上面的结果
                    List<Catelog2Vo> catelog2Vos = null;
                    if (categoryEntities != null) {
                        catelog2Vos = categoryEntities.stream().map(l2 -> {
                            Catelog2Vo catelog2Vo = new Catelog2Vo(v.getCatId().toString(), null, l2.getCatId().toString(), l2.getName().toString());

                            //1、找当前二级分类的三级分类封装成vo
                            List<CategoryEntity> level3Catelog = getParent_cid(selectList, l2.getCatId());

                            if (level3Catelog != null) {
                                List<Catelog2Vo.Category3Vo> category3Vos = level3Catelog.stream().map(l3 -> {
                                    //2、封装成指定格式
                                    Catelog2Vo.Category3Vo category3Vo = new Catelog2Vo.Category3Vo(l2.getCatId().toString(), l3.getCatId().toString(), l3.getName());

                                    return category3Vo;
                                }).collect(Collectors.toList());
                                catelog2Vo.setCatalog3List(category3Vos);
                            }

                            return catelog2Vo;
                        }).collect(Collectors.toList());
                    }
                    return catelog2Vos;
                }));
        //将数据放入缓存
        stringRedisTemplate.opsForValue().set("catalogJSON", JSON.toJSONString(parentCid), 1, TimeUnit.DAYS);

        return parentCid;
    }

    private List<CategoryEntity> getParent_cid(List<CategoryEntity> selectList, Long parentCid) {
        List<CategoryEntity> categoryEntities = selectList.stream().filter(item -> item.getParentCid().equals(parentCid)).collect(Collectors.toList());
        return categoryEntities;
        // return this.baseMapper.selectList(
        //         new QueryWrapper<CategoryEntity>().eq("parent_cid", parentCid));
    }

    private List<Long> findParentPath(Long catelogId, List<Long> paths) {

        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);
        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(), paths);
        }
        return paths;
    }

    //递归查找所有菜单的子菜单
    private List<CategoryEntity> getChildrens(CategoryEntity root, List<CategoryEntity> all) {
        List<CategoryEntity> children = all.stream().filter(categoryEntity -> {
            return Objects.equals(categoryEntity.getParentCid(), root.getCatId());
        }).map(categoryEntity -> {
            //找到子菜单
            categoryEntity.setChildren(getChildrens(categoryEntity, all));
            return categoryEntity;
        }).sorted(Comparator.comparingInt(menu -> (menu.getSort() == null ? 0 : menu.getSort()))).collect(Collectors.toList());
        return children;
    }
}