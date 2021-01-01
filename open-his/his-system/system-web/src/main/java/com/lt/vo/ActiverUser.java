package com.lt.vo;

import com.lt.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//存放在redis的对象
public class ActiverUser implements Serializable {
    private User user;
    private List<String> roles= Collections.EMPTY_LIST;//角色
    private List<String> permissions=Collections.EMPTY_LIST; //权限
}
