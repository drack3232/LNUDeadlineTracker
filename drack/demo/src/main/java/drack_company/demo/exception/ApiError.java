package drack_company.demo.exception;

import java.time.LocalDateTime;

public record ApiError(
        int statusCode,
        String massage,
        LocalDateTime timestamp
) {
}
