package de.fhdw.Kino.Lib.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AuffuehrungDTO implements Serializable {

    private Long auffuehrungId;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime startzeit;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime endzeit;


    @NonNull
    private Long filmId;

    @NonNull
    private Long kinosaalId;
}
