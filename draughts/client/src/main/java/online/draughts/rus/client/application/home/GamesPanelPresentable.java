package online.draughts.rus.client.application.home;

import online.draughts.rus.client.application.widget.popup.DraughtsPlayerPresenter;

/**
 * Created by aleksey on 22.07.16.
 */
public interface GamesPanelPresentable {
    void addToPopupSlot(DraughtsPlayerPresenter draughtsPlayer);

    boolean isPrivatePresenter();
}
