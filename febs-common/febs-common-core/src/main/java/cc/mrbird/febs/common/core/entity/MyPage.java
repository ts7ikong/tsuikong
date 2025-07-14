package cc.mrbird.febs.common.core.entity;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author ZNKJ-R
 * @version V1.0
 * @date 2022/2/16 17:44
 */
public class MyPage<T> extends Page<T> implements IPage<T> {
    private final MyRecords<T> myRecords;

    public MyPage(IPage<T> page) {
        total = page.getTotal();
        size = page.getSize();
        current = page.getCurrent();
        this.records = page.getRecords();
        this.myRecords = new MyRecords<T>(records);
    }

    public MyPage() {
        total = 0;
        size = 0;
        current = 0;
        this.myRecords = new MyRecords<T>();
    }

    public MyRecords<T> getMyRecords() {
        return this.myRecords;
    }

    public static class MyRecords<T> {
        List<T> records = Collections.emptyList();
        List<Map<String, Object>> project = Collections.emptyList();
        List<Map<String, Object>> userList = Collections.emptyList();

        public MyRecords(List<T> records) {
            this.records = records;
        }

        public MyRecords() {
        }

        public List<Map<String, Object>> getUserList() {
            return userList;
        }

        public void setUserList(List<Map<String, Object>> userList) {
            this.userList = userList;
        }

        public List<T> getRecords() {
            return records;
        }

        public void setRecords(List<T> records) {
            this.records = records;
        }

        public List<Map<String, Object>> getProject() {
            return project;
        }

        public void setProject(List<Map<String, Object>> project) {
            this.project = project;
        }
    }
}
