package com.jai.model.response;

import lombok.Data;

/**
 * @author jai
 * created on 07/09/23
 */
@Data
public class GenericResponse<T> {
    ResponseCode status;
    String responseDesc;
    T responseObj;
}
