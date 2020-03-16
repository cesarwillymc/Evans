package com.evans.technologies.usuario.Utils.timeCallback;

import com.google.firebase.database.DataSnapshot;

public interface ComunicateFrag {
    interface mapa_inicio{
        void mensajeGet();
    }
    interface  chat{
        void sendDtaMessage(DataSnapshot snapshot);
    }

}
