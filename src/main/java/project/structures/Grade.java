package project.structures;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * Created by student on 26.02.2017.
 */
@Entity("grades")
@XmlRootElement
public class Grade {

    @Id()
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    @XmlTransient
    private ObjectId id;
    @Property("lId")
    private int lId;
    @Property("course")
    private int idPrzedmiot;
    @Property("student")
    private Integer idStudent;
    @Property("grade")
    private Double stopien;
    @Property("date")
    private Date dataWystawienia;

    public Grade() {

    }

    public void setDataWystawienia(Date dataWystawienia) {

        this.dataWystawienia = dataWystawienia;
    }

    public void setId(ObjectId id){
        this.id = id;
    }

    public void setIdStudent(Integer idStudent) {
        this.idStudent = idStudent;
    }

    public void setIdPrzedmiot(int idPrzedmiot) {
        this.idPrzedmiot = idPrzedmiot;
    }

    @XmlTransient
    public ObjectId getId(){
        return id;
    }

    @XmlElement
    public Integer getIdStudent(){return idStudent;}

    @XmlElement
    public int getIdPrzedmiot(){return idPrzedmiot;}

    @XmlElement
    public Date getDataWystawienia() {

        return dataWystawienia;
    }

    public boolean setStopien(Double stopien) {

        if(stopien>=2&&stopien<=5&&stopien%0.5 == 0) {
            this.stopien = stopien;
            return true;
        }else{
            return false;
        }
    }

    @XmlElement
    public Double getStopien() {

        return stopien;
    }

    @XmlElement
    public int getlId() {
        return lId;
    }

    public void setlId(int lId) {
        this.lId = lId;
    }
}
