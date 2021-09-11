package com.xttdr.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@TableName("course")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    @TableId("course_id")
    private String courseId;
    private String name;
    private String teacherId;
    private Date createdTime;
}
