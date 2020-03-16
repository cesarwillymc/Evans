package com.evans.technologies.usuario.Utils.timeCallback;


import com.evans.technologies.usuario.model.chats;
import com.evans.technologies.usuario.model.sugerenciasLocale;

import java.util.Map;

public interface OnclickItemListener {
     void itemClickNotify(Map<String, Object> notificaciones, int adapterPosition);
    void itemClickChat(chats chat, int adapterPosition);
    void itemClickSuggestion(sugerenciasLocale sugerenciasLocale, int adapterPosition, Boolean tipe_data);

}
