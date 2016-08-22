package com.du.easysignin.sql;

import android.util.Log;

import org.xutils.DbManager;
import org.xutils.DbManager.DaoConfig;
import org.xutils.db.sqlite.SqlInfo;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.db.table.DbModel;
import org.xutils.ex.DbException;

import java.io.File;
import java.util.List;

/**
 * xutils的dbutils工具类
 * 作者：DragonDu
 * Email: 807110586@qq.com
 */
public class DbHelper {
    String mDbname;//数据库名字
    File mDir;//数据库文件地址
    int mDbVersion;
    private static DaoConfig daoConfig;


    //    public DbHelper( String DbName, File dir, int dbVersion) {
////        this.daoConfig = daoConfig;
//        mDbname = DbName;
//        mDir = dir;
//        mDbVersion = dbVersion;
//    }
    //获取数据库配置对象
    public static DaoConfig getDaoconfig(String DbName, File dir, int dbVersion) {
        daoConfig = new DaoConfig()
//                .setDbName(DbName+".db")
                .setDbName(DbName)
                .setDbDir(dir)
                .setDbVersion(dbVersion);

        return daoConfig;
    }

//   public static StuTable getinfo(){
//
//

//    //    }
//    //通过学号id查找
//    public static StuTable getfindById(DbManager db, Class<?> cls, String id) {
//        Object byId = null;
//        try {
//            byId = db.findById(cls, id);
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
//
//        return (StuTable) byId;
//    }
//
//    //相关查找方法
//    public void getHelp(DbManager db) {
//        try {
//            //findall
//            List<StuTable> all = db.findAll(StuTable.class);
//            //findfirst
//            StuTable first = db.findFirst(StuTable.class);
//            //selector
//            db.selector(StuTable.class).where("classname", "=", "1241").and("card_number", "=",
//                    "2010").findAll();
//            //findDbModelFirst
//            DbModel model = db.findDbModelFirst(new SqlInfo("select * from classinfo"));
//            model.getString("classname");
//
//            //findDbModelAll
//            List<DbModel> persons = db.findDbModelAll(new SqlInfo("select * from person where age" +
//                    " > 25"));
//            for (DbModel person : persons) {
//                Log.e("name", person.getString("name"));
//            }
//
//            //修改某一id某一项
//            StuTable person = db.findById(StuTable.class, 1);
//            person.setClassName("1231");
//            db.update(person, "classname");
//            //修改全部的某一项
//            List<StuTable> personsss = db.findAll(StuTable.class);
//            for (StuTable per : personsss) {
//                per.setClassName("1241");
////                db.update(per,WhereBuilder.b("isAdmin","=","0"),"classname");
////                db.update(per,WhereBuilder.b("isAdmin","=","0"),"classname");
//            }
//            //删除 byid
//            db.deleteById(StuTable.class, 5);
//
//            //删除 一个或多条
//            StuTable sti = db.selector(StuTable.class).where("name", "=", "骆驼").findFirst();
//            db.delete(sti);
//
//            //delete(Class<?> entityType)删除表格里面的所有数据，但是注意：表还会存在，只是表里面数据没有了
//            db.delete(StuTable.class);
//
//            //delete(Class<?> entityType, WhereBuilder whereBuilder) 据where语句的条件进行删除操作
//            db.delete(StuTable.class, WhereBuilder.b("sex", "=", "woman").and("salary", "=", "5000"));
//
//            //.dropTable(Class<?> entityType)是用来删除表
//            db.dropTable(StuTable.class);
//
//            //dropDb用来删除数据库
//            db.dropDb();
//
//            //addColumn(Class<> entityType, String column) 表中加入一个country字段
//            db.addColumn(StuTable.class, "country");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
