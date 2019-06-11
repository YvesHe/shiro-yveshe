package com.yveshe.dao;

import java.util.List;

import com.yveshe.entity.Role;

public interface RoleDao {

    public Role createRole(Role role);

    public Role updateRole(Role role);

    public void deleteRole(Long roleId);

    public Role findOne(Long roleId);

    public List<Role> findAll();
}
