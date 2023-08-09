package com.crking7.datn.web.admin;

import com.crking7.datn.services.ArticleService;
import com.crking7.datn.web.dto.request.ArticleRequest;
import com.crking7.datn.web.dto.request.ArticleUDRequest;
import com.crking7.datn.web.dto.request.CategoryRequest;
import com.crking7.datn.web.dto.response.ArticleResponse;
import com.crking7.datn.web.dto.response.CategoryResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/article")
public class AArticleRest {
    private final ArticleService articleService;

    public AArticleRest(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createArticle(@RequestBody ArticleRequest articleRequest) {
        try {
            ArticleResponse articleResponse = articleService.createArticle(articleRequest);
            return ResponseEntity.ok(articleResponse);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateArticle(@PathVariable("id") long id,
                                           @RequestBody ArticleUDRequest articleRequest) {
        try {
            ArticleResponse articleResponse = articleService.updateArticle(id, articleRequest);
            return ResponseEntity.ok(articleResponse);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/hide/{id}")
    public ResponseEntity<?> hideArticle(@PathVariable("id") long id) {
        try {
            ArticleResponse articleResponse = articleService.hideArticle(id);
            if (articleResponse == null) {
                return new ResponseEntity<>("Không tìm thấy bài viết", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Bài viết đã được ẩn", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/show/{id}")
    public ResponseEntity<?> showArticle(@PathVariable("id") long id) {
        try {
            ArticleResponse articleResponse = articleService.showArticle(id);
            if (articleResponse == null) {
                return new ResponseEntity<>("Không tìm thấy Bài viết", HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>("Bài viết đã được hiện", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lỗi!" + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteArticle(@PathVariable(name = "id") long id) {
        try {
            articleService.delete(id);
            return ResponseEntity.ok("Xóa bài viết thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa bài viết không thành công! Lỗi " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteImage/{id}")
    public ResponseEntity<?> deleteArticleImage(@PathVariable(name = "id") long articleImageId) {
        try {
            articleService.deleteImage(articleImageId);
            return ResponseEntity.ok("Xóa ảnh thành công!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Xóa ảnh không thành công! Lỗi " + e.getMessage());
        }
    }
}
