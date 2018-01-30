package ch.liip.timeforcoffee.common.presenter;

public interface Presenter {

    void onResumeView();

    void onRefreshView();

    void onPauseView();

    void onDestroy();

}
