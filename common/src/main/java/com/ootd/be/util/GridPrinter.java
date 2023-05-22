package com.ootd.be.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class GridPrinter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String KR_REGEX = "[ㄱ-ㅎㅏ-ㅣ가-힣]";
    private static final String START_STR = "| ";
    private static final String END_STR = " |";
    private static final String MID_STR = " | ";

    private final Header[] headers;
    private final List<List<Column>> datas = new ArrayList<>();

    private GridPrinter(String...headers) {
        this.headers = Arrays.stream(headers).map(Header::new).toArray(Header[]::new);
    }

    public static GridPrinter of(String...headers) {
        return new GridPrinter(headers);
    }

    public static GridPrinter from(List<Object> objs) {
        if (objs.isEmpty()) throw new RuntimeException("비어있는 데이터");

        Object obj = objs.get(0);
        Map<String, Object> map = OBJECT_MAPPER.convertValue(obj, Map.class);

        GridPrinter printer = GridPrinter.of(map.keySet().stream().map(String::valueOf).toArray(String[]::new));

        objs.forEach(o -> {
            Map<String, Object> t = OBJECT_MAPPER.convertValue(o, Map.class);
            printer.add(map.keySet().stream().map(h -> t.get(h)).toArray(Object[]::new));
        });

        return printer;
    }

    public void add(Object...data) {

        List<Column> d = Arrays.stream(data).map(String::valueOf).map(Column::new).collect(Collectors.toList());
        if (d.size() != headers.length) throw new RuntimeException("헤더랑 개수가 다름 ㅋㅋ");

        datas.add(d);

        for (int i = 0; i < d.size(); i++) {
            headers[i].adjustSize(d.get(i));
        }

    }

    private String header() {
        return START_STR + Arrays.stream(headers).map(h -> h.name.formatString(h.size)).collect(Collectors.joining(MID_STR)) + END_STR;
    }

    private String data(List<Column> data) {
        StringBuilder sb = new StringBuilder(START_STR);
        for (int i = 0; i < data.size(); i++) {
            if (i != 0) sb.append(MID_STR);
            sb.append(data.get(i).formatString(headers[i].size));
        }
        sb.append(END_STR);
        return sb.toString();
    }

    private String frame() {
        int total = Arrays.stream(headers).mapToInt(h -> h.size).sum() + START_STR.length() + END_STR.length() + MID_STR.length() * (headers.length - 1);
        return IntStream.rangeClosed(1, total).mapToObj(i -> "=").collect(Collectors.joining());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(frame()).append(System.lineSeparator())
                .append(header()).append(System.lineSeparator())
                .append(frame()).append(System.lineSeparator());
        datas.forEach(data -> sb.append(data(data)).append(System.lineSeparator()));
        sb.append(frame()).append(System.lineSeparator());
        return sb.toString();
    }

    public void print() {
        System.out.println(frame());
        System.out.println(header());
        System.out.println(frame());
        datas.forEach(data -> {
            System.out.println(data(data));
        });
        System.out.println(frame());
    }

    public void log() {
        log.info(frame());
        log.info(header());
        log.info(frame());
        datas.forEach(data -> {
            log.info(data(data));
        });
        log.info(frame());
    }

    static class Header {

        final Column name;
        int size;

        protected Header(String name) {
            this.name = new Column(name);
            this.size = this.name.width;
        }

        public void adjustSize(Column c) {
            if (c.width > size) this.size = c.width;
        }

    }

    static class Column {
        final String value;
        final int totalCnt;
        final int width;

        protected Column(String value) {
            this.value = value;
            if (StringUtils.hasLength(value)) {
                this.totalCnt = value.length();
                int etcCnt = value.replaceAll(KR_REGEX, "").length();
                int krCnt = this.totalCnt - etcCnt;
                this.width = etcCnt + krCnt * 2;
            } else {
                this.totalCnt = 0;
                this.width = 0;
            }
        }

        public String formatString(int size) {
            return String.format("%-" + ((size - width) + totalCnt) + "s", this.value);
        }
    }

}
