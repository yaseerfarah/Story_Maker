package ja.burhanrashid52.photoeditor;

import android.view.View;

import ja.burhanrashid52.photoeditor.ViewType;

public class LayerItem {

    private ViewType type;
    private View view;

    public LayerItem(ViewType type, View view) {
        this.type = type;
        this.view = view;
    }

    public ViewType getType() {
        return type;
    }

    public View getView() {
        return view;
    }
}
