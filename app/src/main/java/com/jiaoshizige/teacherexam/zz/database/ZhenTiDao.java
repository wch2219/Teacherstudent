package com.jiaoshizige.teacherexam.zz.database;

import android.content.Context;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.jiaoshizige.teacherexam.zz.AllQuestionsActivity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;


/**
 * 操作article表的DAO类
 */
public class ZhenTiDao {
    private Context context;
    // ORMLite提供的DAO类对象，第一个泛型是要操作的数据表映射成的实体类；第二个泛型是这个实体类中ID的数据类型
    private Dao<ZhenTi, Integer> dao;

    public ZhenTiDao(Context context) {
        this.context = context;
        try {
            this.dao = DatabaseHelper.getInstance(context).getDao(ZhenTi.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 添加数据
    public void insert(ZhenTi data) {
        try {
            dao.create(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // 删除数据
    public void delete(ZhenTi data) {
        try {
            dao.delete(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 修改数据
    public void update(ZhenTi data) {
        try {
            dao.update(data);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 通过ID查询一条数据
    public ZhenTi queryById(int id) {
        ZhenTi article = null;
        try {
            article = dao.queryForId(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return article;
    }

    // 通过条件查询文章集合（通过用户ID查找）
    public List<ZhenTi> queryByUserId(int user_id) {
        try {
            return dao.queryBuilder().where().eq(ZhenTi.COLUMNNAME_ID, user_id).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //根据columnName查询
    public List<ZhenTi> queryByColumnName(String columnName, String value) {
        List<ZhenTi> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().where().eq(columnName, value).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ZhenTi> queryByColumnNameAndColumnName(String columnName1, String value1, String columnName2, String value2) {
        List<ZhenTi> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().where().eq(columnName1, value1).and().eq(columnName2, value2).query();//参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public List<ZhenTi> orderBy(String columnName, boolean desc, String columnName1, String value1, String columnName2, String value2) {
        List<ZhenTi> list = new ArrayList<>();
        try {
            list = dao.queryBuilder().orderBy(columnName, desc).where().eq(columnName1, value1).and().eq(columnName2, value2).query();//参数false表示降序，true表示升序。
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
