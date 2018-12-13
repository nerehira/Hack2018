package nerehira.proyect.Modelos;

public class detalle {
    String location;
    String type;
    String id;
    String photo;
    String buses;

    String status;



    public detalle(){}
    public detalle(String location, String type, String id, String photo, String buses,String status) {
        this.location = location;
        this.type = type;
        this.id = id;
        this.photo = photo;
        this.buses = buses;

        this.status = status;

    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }




    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBuses() {
        return buses;
    }

    public void setBuses(String buses) {
        this.buses = buses;
    }




}
