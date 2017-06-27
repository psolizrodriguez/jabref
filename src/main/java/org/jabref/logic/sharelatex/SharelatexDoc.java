package org.jabref.logic.sharelatex;

public class SharelatexDoc {

    private int position;
    private String content;
    private String op;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setOperation(String opType) {
        this.op = opType;
    }

    @Override
    public String toString()
    {
        return position + " " + content;
    }
}
