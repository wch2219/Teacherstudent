package com.jiaoshizige.teacherexam.zz.database;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 操作User数据表的Dao类，封装这操作User表的所有操作
 * 通过DatabaseHelper类中的方法获取ORMLite内置的DAO类进行数据库中数据的操作
 * <p>
 * 调用dao的create()方法向表中添加数据
 * 调用dao的delete()方法删除表中的数据
 * 调用dao的update()方法修改表中的数据
 * 调用dao的queryForAll()方法查询表中的所有数据
 */
public class ZhenTiListDao {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<ZhenTiList, Integer> dao;

    public ZhenTiListDao(Context context) {
        this.context = context;
        try {
            this.dao = DatabaseHelper.getInstance(context).getDao(ZhenTiList.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 向user表中添加一条数据
    public void insert(ZhenTiList data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 删除user表中的一条数据
    public void delete(ZhenTiList data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改user表中的一条数据
    public void update(ZhenTiList data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 查询user表中的所有数据
    public List<ZhenTiList> selectAll() {
        List<ZhenTiList> users = null;
        try {
            users = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // 根据ID取出用户信息
    public ZhenTiList queryById(int id) {
        ZhenTiList user = null;
        try {
            user = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<ZhenTiList> queryAll() {
        List<ZhenTiList> zhenTiList = new ArrayList<>();
        try {
            zhenTiList = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return zhenTiList;
    }

    public List<ZhenTiList> queryByColumnName(String columnName, String value) {
        List<ZhenTiList> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ZhenTiList> queryByColumnNameAndColumnName(String columnName1, String value1, String columnName2, String value2) {
        List<ZhenTiList> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().where().eq(columnName1, value1).and().eq(columnName2, value2).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ZhenTiList> queryByColumnNames(String columnName1, String value1, String columnName2, String value2, String columnName3, String value3) {
        List<ZhenTiList> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().where().eq(columnName1, value1).and().eq(columnName2, value2).and().eq(columnName3, value3).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
