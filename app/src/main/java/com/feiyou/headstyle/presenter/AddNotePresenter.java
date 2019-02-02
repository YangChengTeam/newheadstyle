package com.feiyou.headstyle.presenter;

import java.util.List;

/**
 * Created by iflying on 2018/1/9.
 */

public interface AddNotePresenter {
    void addNote(String uid, String content, String topicId, String friends,List<String>filePaths);
}
