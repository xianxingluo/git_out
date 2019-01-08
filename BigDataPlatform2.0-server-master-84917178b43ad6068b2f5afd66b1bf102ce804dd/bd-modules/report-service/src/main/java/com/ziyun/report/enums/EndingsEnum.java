package com.ziyun.report.enums;

/**
 * @description:毕业生标签
 */
public enum EndingsEnum {
    FIRST("1", "博观约取、厚积薄发，愿你拥有灿烂年华！"),
    SECOND("2", "唯有不断努力让自己变得更加完美！"),
    THIRD("3", "您多次斩获荣誉，愿你在社会大舞台勇创佳绩！"),
    FOURTH("4", "您拥有骄人的学业成绩，愿你在社会大舞台勇创佳绩！"),
    FIFTH("5", "网络诚有趣，若为生活故，现实最重要！"),
    SIXTH("6", "愿您能够把握好未来航向，适当游戏！");
/*
    FIRST("1", "爱读书的你，一定很幸福吧！博观约取、厚积薄发，愿你拥有灿烂年华！"),
    SECOND("2", "金无足赤，人无完人，唯有不断努力让自己变得更加完美！"),
    THIRD("3", "优秀的你，多次斩获荣誉，愿你不断进取，在社会大舞台勇创佳绩！"),
    FOURTH("4", "优秀的你，拥有骄人的学业成绩，愿你不断前行，在社会大舞台勇创佳绩！"),
    FIFTH("5", "网络诚有趣，手机确实好，若为生活故，现实最重要！"),
    SIXTH("6", "适度游戏益脑，沉迷游戏伤身，把握未来航向，从此奋发图强！");
*/
    private String key;
    private String endings;

    EndingsEnum(String key, String endings) {
        this.key = key;
        this.endings = endings;
    }

    public static EndingsEnum get(String key) {
        EndingsEnum[] values = EndingsEnum.values();
        for (EndingsEnum object : values) {
            if (object.key.equals(key)) {
                return object;
            }
        }
        return null;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getEndings() {
        return endings;
    }

    public void setEndings(String endings) {
        this.endings = endings;
    }
}
