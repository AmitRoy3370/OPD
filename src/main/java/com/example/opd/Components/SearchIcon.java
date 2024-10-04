package com.example.opd.Components;

import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.util.Resources;

public interface SearchIcon {

    Image searchIcon = Resources.getGlobalResources().getImage("search_icon.jpeg");

    Label searchIconLabel = new Label(searchIcon);

}
