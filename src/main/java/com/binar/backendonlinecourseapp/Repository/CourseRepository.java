package com.binar.backendonlinecourseapp.Repository;

import com.binar.backendonlinecourseapp.Entity.Category;
import com.binar.backendonlinecourseapp.Entity.Course;
import com.binar.backendonlinecourseapp.Entity.Enum.ClassType;
import com.binar.backendonlinecourseapp.Entity.Enum.Level;
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

    @Query("SELECT c FROM Course c WHERE LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Course> findByClassNameOrTeacherJPQL(@Param("searchTerm") String searchTerm);

    @Query("SELECT c FROM Course c WHERE LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<Course> findByClassNameOrTeacherJPQLPage(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE (LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND c.classType = 'PREMIUM'")
    List<Course> findByClassNameOrAuthorAndClassType(String searchTerm);

    @Query("SELECT c FROM Course c WHERE (LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND c.classType = 'PREMIUM'")
    Page<Course> findByClassNameOrAuthorAndClassTypepage(String searchTerm, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE (LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND c.classType = 'FREE'")
    List<Course> findByClassNameOrAuthorAndClassTypeFree(String searchTerm);

    @Query("SELECT c FROM Course c WHERE (LOWER(c.className) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR LOWER(c.author) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND c.classType = 'FREE'")
    Page<Course> findByClassNameOrAuthorAndClassTypeFreepage(String searchTerm, Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.classType = 'PREMIUM'")
    List<Course> findPremiumCourses();

    @Query("SELECT c FROM Course c WHERE c.classType = 'PREMIUM'")
    Page<Course> findPremiumCourses(Pageable pageable);

    @Query("SELECT c FROM Course c WHERE c.classType = 'FREE'")
    List<Course> findFreeCourses();

    @Query("SELECT c FROM Course c WHERE c.classType = 'FREE'")
    Page<Course> findFreeCourses(Pageable pageable);

    @Query("SELECT c FROM Course c JOIN c.chapters ch JOIN ch.videos v WHERE v.videoCode = :videoCode")
    Optional<Course> findCourseByVideoCode(@Param("videoCode") String videoCode);

    @Query("SELECT c FROM Course c ORDER BY c.publish DESC")
    List<Course> findAllOrderByPublishDesc();

    @Query("SELECT c FROM Course c ORDER BY c.rating DESC")
    List<Course> findAllOrderBypopularDesc();

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "ORDER BY " +
            "CASE WHEN :orderByRating = true THEN c.rating END DESC, " +
            "CASE WHEN :orderByPublish = true THEN c.publish END DESC")
    List<Course> findFilteredCoursesWithoutclasstype(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList,
            @Param("orderByRating") boolean orderByRating,
            @Param("orderByPublish") boolean orderByPublish);

    @Query("SELECT c FROM Course c " +
            "WHERE (COALESCE(:categoryList, NULL) IS NULL OR c.categories IN :categoryList) " +
            "AND (COALESCE(:levelList, NULL) IS NULL OR c.level IN :levelList) " +
            "AND (COALESCE(:classType, NULL) IS NULL OR c.classType = :classType) " +
            "ORDER BY " +
            "CASE WHEN :orderByRating = true THEN c.rating END DESC, " +
            "CASE WHEN :orderByPublish = true THEN c.publish END DESC")
    List<Course> findFilteredCourses(
            @Param("categoryList") List<Category> categoryList,
            @Param("levelList") List<Level> levelList,
            @Param("classType") ClassType classType,
            @Param("orderByRating") boolean orderByRating,
            @Param("orderByPublish") boolean orderByPublish);

    @Query("SELECT c FROM Course c WHERE c.rating >= 3.5")
    List<Course> findByRatingGreaterThanOrEqual();

    @Query("SELECT c FROM Course c WHERE c.rating >= 3.5 AND c.categories = :category")
    List<Course> findByRatingAndCategory(
            @Param("category") Category category
    );

}

