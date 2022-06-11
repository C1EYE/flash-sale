package cn.wolfcode.domain;

import lombok.Data;
import java.io.Serializable;


@Data
public class UserLogin implements Serializable {
    private Long phone;//手机号码
    private String password;//密码
    private String salt;//加密使用的盐

}
