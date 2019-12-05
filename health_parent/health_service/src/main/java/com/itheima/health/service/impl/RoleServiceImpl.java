package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = RoleService.class)
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;

    //分页
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage, pageSize);
        Page<Role> roles = roleDao.findPage(queryString);
        return new PageResult(roles.getTotal(), roles.getResult());
    }

    //查找所有权限
    @Override
    public List<Permission> findPermission() {
        return permissionDao.findAll();
    }
    @Override
    //查找所有菜单
    public List<Menu> findMenu() {
        //只显示子菜单
        List<Menu> all = menuDao.findAll();
        all.removeIf(menu -> menu.getParentMenuId() == null);
        return all;
    }
    @Override
    //添加角色
    public void add(Role role, Integer[] permissionIds, Integer[] menuIds) {
        if (role.getKeyword() == null || "".equals(role.getKeyword())) {
            throw new RuntimeException("关键字不能为空");
        }
        if (role.getName() == null || "".equals(role.getName())) {
            throw new RuntimeException("您创建的角色需要一个名字");
        }
        //对role表进行添加
        roleDao.add(role);

        roleAddMenu(role, permissionIds, menuIds);

    }

    private void roleAddMenu(Role role, Integer[] permissionIds, Integer[] menuIds) {
        //添加中间表数据
        if (permissionIds.length < 1 || permissionIds == null) {
            throw new RuntimeException("请给您创建的角色添加权限");
        }
        for (Integer permissionId : permissionIds) {
            roleDao.roleAddPermission(role.getId(), permissionId);
        }


        if (menuIds.length < 1 || menuIds == null) {
            throw new RuntimeException("请给您创建的角色添加可以访问的菜单");
        }

        //查询出父菜单并添加到子菜单整合
        List<Integer> integers = Arrays.asList(menuIds);
        List<Integer> fumenuIds = roleDao.findParentMenu(integers);


        for (Integer menuId : menuIds) {
            //根据id查找menu 判断
            if (menuId!=1&&menuId!=5&&menuId!=11&&menuId!=13&&menuId!=15) {
            roleDao.roleAddmenu(role.getId(), menuId);
            }
        }

        //添加父菜单
        for (Integer menuId : fumenuIds) {
            //根据id查找menu 判断
            if (menuId!=null) {
                roleDao.roleAddmenu(role.getId(), menuId);
            }
        }
    }
    @Override
    //删除角色
    public void delete(int id) {
        //删除之前需清除中间表中的关联数据
        roleDao.removePremission(id);
        roleDao.removeMenu(id);
        roleDao.delete(id);
    }
    @Override
    ////根据id查找
    public HashMap<String, Object> findById(int id) {
//    返回一个map集合
        Role byId = roleDao.findById(id);
        ArrayList<Integer> permissionIds = new ArrayList<>();
        Set<Permission> permissions = byId.getPermissions();
        for (Permission permission : permissions) {
            Integer permissionId = permission.getId();
            permissionIds.add(permissionId);
        }
        LinkedHashSet<Menu> menus = byId.getMenus();
        ArrayList<Integer> menuIds = new ArrayList<>();
        for (Menu menu : menus) {
            Integer menuId = menu.getId();
            menuIds.add(menuId);
        }
        HashMap<String, Object> result = new HashMap<>();
        result.put("role", byId);
        result.put("permissionIds", permissionIds);
        result.put("menuIds", menuIds);
        return result;
    }
    @Override
    //编辑role
    public void edit(Role role, Integer[] permissionIds, Integer[] menuIds) {
        if (role.getKeyword() == null || "".equals(role.getKeyword())) {
            throw new RuntimeException("关键字不能为空");
        }
        if (role.getName() == null || "".equals(role.getName())) {
            throw new RuntimeException("您编辑的角色需要一个名字");
        }
        //对role表进行编辑
        roleDao.edit(role);
        //清除中间表
        roleDao.removePremission(role.getId());
        roleDao.removeMenu(role.getId());

        roleAddMenu(role, permissionIds, menuIds);

    }
}
