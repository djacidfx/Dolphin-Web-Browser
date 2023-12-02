package com.codebyte.dolphinwebbrowser.VdstudioAppModel;

public class FileData {
    String creation;
    String modified;
    String name;
    String parent;
    String path;
    String size;

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getModified() {
        return this.modified;
    }

    public void setModified(String str) {
        this.modified = str;
    }


    public void setCreation(String str) {
        this.creation = str;
    }


    public void setParent(String str) {
        this.parent = str;
    }
}
