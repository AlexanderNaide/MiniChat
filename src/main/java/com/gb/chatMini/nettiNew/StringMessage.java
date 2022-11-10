package com.gb.chatMini.nettiNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;


@Data                                 // Вот тут будет ругаться без нижней анотации
@EqualsAndHashCode(callSuper = true)  // Вот тут мы даем lombok понять, где делать иквалс и хешкод - в родителе или в потомке (в данном случае в родителе)
@AllArgsConstructor
public class StringMessage extends AbstractMessage{

    private String content;
    private LocalDateTime time;
}
