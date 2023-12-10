package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Level;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, String> {

    Page<Course> findAll(Pageable pageable);

    Optional<Course> findByCourseCode(String code);

    Optional<Course> findByClassName(String code);

    @Query("SELECT c FROM Course c WHERE c.className LIKE %:searchTerm% OR c.author LIKE %:searchTerm%")
    List<Course> findByClassNameOrTeacherJPQL(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Course c WHERE c.classType = 'PREMIUM'")
    List<Course> findPremiumCourses();

    @Query("SELECT c FROM Course c WHERE c.classType = 'FREE'")
    List<Course> findFreeCourses();

    @Query("SELECT c FROM Course c JOIN c.videos v WHERE v.videoCode = :videoCode")
    Optional<Course> findCourseByVideoCode(@Param("videoCode") String videoCode);

    @Query("SELECT c FROM Course c ORDER BY c.publish DESC")
    List<Course> findAllOrderByPublishDesc();

    @Query("SELECT c FROM Course c ORDER BY c.rating DESC")
    List<Course> findAllOrderBypopularDesc();

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) ")
    List<Course> findFilteredCourses(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList);

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "ORDER BY c.publish DESC")
    List<Course> findFilteredCoursesIsNewest(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList);

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "ORDER BY c.rating DESC")
    List<Course> findFilteredCoursesIsPopular(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList);

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "ORDER BY c.rating DESC, c.publish DESC")
    List<Course> findFilteredCoursesBothIsTrue(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList);

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "ORDER BY " +
            "CASE WHEN :orderByRating = true THEN c.rating END DESC, " +
            "CASE WHEN :orderByPublish = true THEN c.publish END DESC")
    List<Course> findFilteredCourses(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList,
            @Param("orderByRating") boolean orderByRating,
            @Param("orderByPublish") boolean orderByPublish);

}

