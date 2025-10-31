package com.example.webtoon.health;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//오라클 DB연결 확인용
@RestController
public class DbCheckController {
    private final JdbcTemplate jdbc;

    public DbCheckController(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @GetMapping("/api/dbcheck")
    public String dbcheck() {
        Integer one = jdbc.queryForObject("SELECT 1 FROM DUAL", Integer.class);
        return (one != null && one == 1) ? "db-ok" : "db-fail";
    }
}
