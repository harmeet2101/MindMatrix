package Model;

/**
 * Created by ganesha on 17/3/17.
 */

public class DrawerMenuItem {

    int id;
    String menuName;
    int menuType;
    String menuIcon;

    public DrawerMenuItem(int id, String menuName, int menuType, String menuIcon) {

        this.id = id;
        this.menuName = menuName;
        this.menuType = menuType;
        this.menuIcon = menuIcon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuType() {
        return menuType;
    }

    public void setMenuType(int menuType) {
        this.menuType = menuType;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }
}
