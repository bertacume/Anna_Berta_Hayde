package upc.eet.pma.travelapp;

public class Category {
    private String name;
    //private int categoryId;
    private String description;


    public Category() {
        super();
    }

    public Category( /*int categoryId, */String name, String description) {
        super();
        this.name = name;
        this.description = description;
        //this.categoryId = categoryId;
    }


    public String getName() {
        return name;
    }

    public void setTittle(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


/*
    public int getCategoryId(){return categoryId;}

    public void setCategoryId(int categoryId){this.categoryId = categoryId;}
    */
}
