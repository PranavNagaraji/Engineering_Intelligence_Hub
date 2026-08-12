package com.pranav.engineering_intelligence_hub.specification;

import org.springframework.data.jpa.domain.Specification;

import com.pranav.engineering_intelligence_hub.entity.Role;
import com.pranav.engineering_intelligence_hub.entity.User;

public class UserSpecifications {
    public static Specification<User> hasRole(Role role){
        return (root, query, cb) -> cb.equal(root.get("role"), role);
    }
    public static Specification<User> hasUsername(String username){
        return (root, query, cb) -> cb.equal(root.get("username"), username);
    }
    public static Specification<User> hasUsernameLike(String username){
        return (root, query, cb) -> cb.like(root.get("username"), "%"+username+"%");
    }
}
