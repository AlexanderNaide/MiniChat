package com.gb.chatMini.nettiNew;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;


@Data                                 // ��� ��� ����� �������� ��� ������ ��������
@EqualsAndHashCode(callSuper = true)  // ��� ��� �� ���� lombok ������, ��� ������ ������ � ������ - � �������� ��� � ������� (� ������ ������ � ��������)
@AllArgsConstructor
public class StringMessage extends AbstractMessage{

    private String content;
    private LocalDateTime time;
}
