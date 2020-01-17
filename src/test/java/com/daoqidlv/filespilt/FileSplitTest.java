package com.daoqidlv.filespilt;

import org.junit.Test;

import static org.junit.Assert.*;

public class FileSplitTest {

    @Test
    public void splitByKeyword() {
        String fileDir = "/Users/fangwenjun/IdeaProjects/filespilt-demo";
        String fileName = "test.txt";
        String mode = "NORMAL";
        String splitSymbol = "@!@";
        String splitKeyWordLocation = "0";
        String[] args = new String[] {fileDir, fileName, mode, splitSymbol, splitKeyWordLocation, "b", "a"};
        FileSplit.fileSplitByKeyword(args);
    }

}