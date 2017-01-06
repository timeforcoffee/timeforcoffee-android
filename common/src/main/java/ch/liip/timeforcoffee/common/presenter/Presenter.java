package ch.liip.timeforcoffee.common.presenter;

/**
 * Created by nicolas on 23/12/16.
 */
public interface Presenter {
    void onResumeView();

    void onRefreshView();

    void onPauseView();

    void onDestroy();
}
