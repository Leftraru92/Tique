package uai.diploma.tique.util;

import uai.diploma.tique.R;

public class Util {
    public static int getIcon(String icon) {

        int rIcon;

        switch (icon) {
            case Constantes.I_STORE:
                rIcon = R.drawable.ic_store_mall_directory_black_24dp;
                break;
            case Constantes.I_WALK:
                rIcon = R.drawable.ic_directions_walk_black_24dp;
                break;
            case Constantes.I_DINNER:
                rIcon = R.drawable.ic_local_dining_black_24dp;
                break;
            case Constantes.I_BAR:
                rIcon = R.drawable.ic_local_bar_black_24dp;
                break;
            case Constantes.I_CAFE:
                rIcon = R.drawable.ic_local_cafe_black_24dp;
                break;
            case Constantes.I_PIZZA:
                rIcon = R.drawable.ic_local_pizza_black_24dp;
                break;
            case Constantes.I_TICKET:
                rIcon = R.drawable.ic_local_activity_black_24dp;
                break;
            default:
                rIcon = R.drawable.ic_home_black_24dp;
                break;
        }
        return  rIcon;

    }
}
