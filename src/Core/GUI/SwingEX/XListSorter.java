package Core.GUI.SwingEX;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * KM
 * June 04 2017
 * A special (new!) class designed to create and sort items in a list, similar to a Swing layout, but with more function.
 * It can create either a vertical or horizontal list of items, and use either preset spacing or custom spacing, among other things.

 * SOURCES:
 * None in specific. Completely custom created through previously acquired knowledge.
 *
 * CONCEPTS:
 * Swing, ArrayLists, Classes/Objects, for loops, try/catch block, if/else, case statements
 */

public class XListSorter implements XConstants {

    /**
     Variable declarations.
     */

    private ArrayList<JComponent> list = new ArrayList<>(); //items that will be sent for placement
    private int sizex, sizey, posx, posy, space, sort;
    private boolean resize = false;
    private boolean autosize = false;

    //------------------------------------------------------------------------------------------------------------------
    /**
     Constructors.
     */

    public XListSorter() {} //blank constructor

    public XListSorter(int sort) { this.sort = sort; }

    public XListSorter(int sort, int space) {
        this.space = space;
        this.sort = HORIZONTAL_SORT;
    }

    public XListSorter(int sort, int x, int y) {
        this.posx = x;
        this.posy = y;
        this.sort = sort;
    }

    public XListSorter(int sort, int space, int x, int y) {
        this.posx = x;
        this.posy = y;
        this.sort = sort;
        this.space = space;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
    Container editor methods.
     */

    public void forceItemSize(int x, int y) { this.sizex = x; this.sizey = y; this.autosize = true; } //sets the scale of the items in the list
    public void setLocation(int x, int y) { this.posx = x; this.posy = y; } //sets the list's position on the component
    public void setItemSpacing(int space) { this.space = space; } //spacing between components
    public void scaleParent(boolean b) { this.resize = b; } //whether or not the list will resize the container to fit the items (default false)
    public void setAlignment(int sortStyle) { this.sort = sortStyle; } //sets the alignment of the sorter

    public boolean isAutoSizing() { return autosize; }
    public boolean isResizing() { return resize; }

    public int getPosX() { return posx; }
    public int getPosY() { return  posy; }
    public int getSizeX() { return sizex; }
    public int getSizeY() { return  sizey; }
    public int getSortStyle() { return sort; }
    public int getSpace() { return space; }

    public void resizeParent(boolean b) { this.resize = b; }

    //------------------------------------------------------------------------------------------------------------------
    /**
     List editor variables.
     */

    public void addItem(JComponent c) { list.add(c); } //adds a component to the list sorter
    public void addItem(int layer, JComponent item) { list.add(layer, item); } //adds an items to a specific layer
    public void addItems(JComponent... c) { list.addAll(Arrays.asList(c)); } //adds a group of components to the list sorter
    public void replaceItem(JComponent newComponent, JComponent oldComponent) { list.add(list.indexOf(oldComponent), newComponent); } //replaces an item in the list

    public void removeItem(JComponent c) { list.remove(c); }
    public void removeItem(int loc) { list.remove(loc); }
    public void removeAll() { list.clear(); }

    public JComponent getItem(int arraypos) { return list.get(arraypos); }
    public JComponent getItem(JComponent c) { return list.get(list.indexOf(c)); } //gets a component from the list
    public int getItemCount() { return list.size(); } //returns the number of items in the sorter
    public int getItemIndex(JComponent component) { return list.indexOf(component); } //returns the index of an item in the list

    //------------------------------------------------------------------------------------------------------------------
    /**
     Main methodology.
     */

    public void placeItems(JComponent c) { //adds the components to the specified component

        if (list.size() == 0) { //no items > don't attempt to place
            System.out.println("XListSorter - No items in the queue, aborting item placer.");
            return;
        }

        for (JComponent item : list) { //get the inputted components
            try { //attempt to add item to the specified component
                c.add(item);
            } catch (Exception e) { //if it errors during generation, skip it and move to the next iteration
                System.out.println("ListSorter component was not initialized: " + e.getMessage());
                continue;
            }

            if (autosize) { //force specific scale on components
                item.setSize(sizex, sizey);
            } else { //use component's existing size
                item.setSize(item.getPreferredSize());
            }

            switch (sort) {
                case HORIZONTAL_SORT:
                    item.setLocation(posx, posy);
                    posx += item.getWidth() + space;
                    break;
                case VERTICAL_SORT:
                    item.setLocation(posx, posy);
                    posy += item.getHeight() + space;
                    break;
                case VERTICAL_SORT_REVERSE:
                    posy = item.getY() - (item.getHeight() + space);
                    item.setLocation(posx, posy);
                    break;
                case HORIZONTAL_SORT_REVERSE:
                    posx = item.getX() - (item.getWidth() + space);
                    item.setLocation(posx, posy);
                    break;
                default:
                    System.out.println("ListSorter sort method not properly specified. Defaulting to HORIZONTAL_SORT.");
                    item.setLocation(posx, posy);
                    posx += item.getWidth() + space;
                    break;
            }

            if (resize) { //if the sorter is allowed to resize the parent container, resize it when necessary
                if (posx + space > c.getWidth() && sort == HORIZONTAL_SORT) {
                    c.setPreferredSize(new Dimension(posx + space, c.getHeight()));
                    c.setSize(posx + space, c.getHeight());
                } else if (posy + space > c.getHeight() && sort == VERTICAL_SORT) {
                    c.setPreferredSize(new Dimension(c.getWidth(), posy + space));
                    c.setSize(c.getWidth(), posy + space);
                }
                //todo: add auto-sizing for reverse sorts
            }

            item.setVisible(true);
        }

    } //placeItems method complete


}