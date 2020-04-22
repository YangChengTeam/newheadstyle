package com.feiyou.headstyle.presenter;

/**
 * Created by iflying on 2018/1/9.
 */

public interface NoteDataPresenter {
    void getNoteData(int page, int pageSize, int type, String userid);

    void getMyNoteList(int page, int pageSize, String userid);
}
