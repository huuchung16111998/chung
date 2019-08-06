package com.smile.studio.libsmilestudio.utils;

import android.location.Location;

public interface IAction {

    interface Factory {
        interface Focus {
            void callback();
        }

        interface Backup {
            void callback();
        }

        interface GPS {
            void callback(Location location);
        }

        interface ActionObject {
            void callback(Object object);
        }

        interface Action {
            void callback();
        }

    }
}
