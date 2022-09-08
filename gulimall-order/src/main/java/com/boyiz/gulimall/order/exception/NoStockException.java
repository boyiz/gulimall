package com.boyiz.gulimall.order.exception;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 无库存抛出的异常
 **/

public class NoStockException extends RuntimeException {

    @Getter @Setter
    private Long skuId;

    public NoStockException(String msg) {
        super(msg);
    }


}