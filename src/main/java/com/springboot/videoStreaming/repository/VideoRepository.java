package com.springboot.videoStreaming.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.springboot.videoStreaming.model.Video;

public interface VideoRepository extends JpaRepository<Video, String>{
	Video  findByTitle(String title);
	List<Video> findByCourseId(String courseId);
	
	List<Video> findByTitleContainingIgnoreCase(String title);
	//ContainingIgnoreCase allows case-insensitive partial matching for the title.
	
	@Query("SELECT v FROM Video v WHERE LOWER(v.title) LIKE LOWER(CONCAT('%', :query, '%')) OR LOWER(v.description) LIKE LOWER(CONCAT('%', :query, '%'))")
	List<Video> searchByTitleOrDescription(@Param("query") String query);
	//we want to search for videos using both title and description, we can't just rely on a 
	//single @RequestParam. Instead, we use a custom query with JPQL (@Query) to perform the search in both fields.
	//You can completely avoid @Query and use Spring Data JPA's method  can use findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

//Using @Query, we can write a custom query that checks both fields with case-insensitive partial matching.
	
//LOWER(v.title) → Converts title to lowercase (for case-insensitive search).
//LIKE LOWER(CONCAT('%', :query, '%')) → Matches any part of the title or description containing query.
//OR LOWER(v.description) LIKE ... → Also searches in the description.

//	Page<Video> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title,
//			String description, Pageable pageable);
	Page<Video> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description,
			Pageable pageable);
	//Spring Data JPA does not automatically support field updates without fetching the entity first. you can use a custom method with @Modifying and @Transactional to directly update the likes count without fetching the entire Video object.
	 @Modifying
	 @Transactional
	 @Query("UPDATE Video v SET v.likes = v.likes + 1 WHERE v.videoId = :videoId")
	 int incrementLikes(@Param("videoId") String videoId);

}
