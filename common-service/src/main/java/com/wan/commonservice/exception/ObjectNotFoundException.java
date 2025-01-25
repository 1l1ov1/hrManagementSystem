package com.wan.commonservice.exception;

import com.wan.commonservice.enums.ResponseStatusCodeEnum;

public class ObjectNotFoundException extends CommonException{

    public ObjectNotFoundException(String message, ResponseStatusCodeEnum code) {
        super(message, code);
    }

    public ObjectNotFoundException(String message, Throwable cause, ResponseStatusCodeEnum code) {
        super(message, cause, code);
    }

    public ObjectNotFoundException(Throwable cause, ResponseStatusCodeEnum code) {
        super(cause, code);
    }

    public ObjectNotFoundException(String message) {
        super(message, ResponseStatusCodeEnum.OBJECT_NOT_FOUNT);
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
