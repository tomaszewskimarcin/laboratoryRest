import project.structures.Course;
import project.structures.Grade;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by student on 26.02.2017.
 */
@Path("/courses")
public class Courses {

    public Courses(){}

    /*@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCourses() {
        ArrayList<Course> cou= new ArrayList<Course>();

        for(Iterator i = Model.getInstance().getCourses().entrySet().iterator(); i.hasNext();) {
            Map.Entry para = (Map.Entry) i.next();
            Course pr = (Course) para.getValue();
            cou.add(pr);
        }
        if(cou.size() > 0) {
            GenericEntity<ArrayList<Course>> courses = new GenericEntity<ArrayList<Course>>(cou){};
                return Response.status(200).header("Success","fetched").entity(courses).build();
        }else{
            return Response.status(404).build();
        }
    }*/

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getCourses() throws ClassNotFoundException {
        org.mongodb.morphia.query.Query<Course> query = Model.getInstance().datastore.createQuery(Course.class);
        ArrayList<Course> cou = new ArrayList<Course>();
        if(query.asList() != null)cou = (ArrayList<Course>) query.asList();
        try{
            GenericEntity<ArrayList<Course>> courses = new GenericEntity<ArrayList<Course>>(cou){};
            return Response.status(200).header("Success","fetched").entity(courses).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response getAllCourses(@QueryParam("teacher") String teacher) {
        org.mongodb.morphia.query.Query<Course> query = Model.getInstance().datastore.createQuery(Course.class);
        if(teacher != null) {
            query.field("teacher").equal(teacher);
        }
        ArrayList<Course> cou = new ArrayList<Course>();
        if(query.asList() != null)cou = (ArrayList<Course>) query.asList();
        try{
            GenericEntity<ArrayList<Course>> courses = new GenericEntity<ArrayList<Course>>(cou){};
            return Response.status(200).header("Success","fetched").entity(courses).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getPrzedmiot(@PathParam("id") int id){
        //Course pr = Model.getInstance().getCourses().get(id);
        org.mongodb.morphia.query.Query<Course> query = Model.getInstance().datastore.createQuery(Course.class).field("lId").equal(id);
        Course pr = new Course();
        if(query.asList() != null)pr = query.get();
        try{
            return Response.status(200).header("Success","fetched").entity(pr).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    /*@GET
    @Path("/{id}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getOcenyPrzedmiotu(@PathParam("id") int id){
        ArrayList<Grade> oc= new ArrayList<Grade>();
        HashMap<Integer, Grade> oceny = Model.getInstance().getGrades();
        for(Iterator iG = oceny.entrySet().iterator(); iG.hasNext();){
            Map.Entry para = (Map.Entry) iG.next();
            Grade gr = (Grade) para.getValue();
            if(gr.getIdPrzedmiot() == id){
                oc.add(gr);
            }
        }
        if(oc.size() > 0) {
            GenericEntity<ArrayList<Grade>> grades = new GenericEntity<ArrayList<Grade>>(oc){};
                return Response.status(200).header("Success","fetched").entity(grades).build();
        }else{
            return Response.status(404).build();
        }
    }*/

    @GET
    @Path("/{id}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getOcenyPrzedmiotu(@PathParam("id") int id){
        org.mongodb.morphia.query.Query<Grade> query = Model.getInstance().datastore.createQuery(Grade.class).field("course").equal(id);
        ArrayList<Grade> oc = new ArrayList<Grade>();
        if(query.asList() != null)oc = (ArrayList<Grade>) query.asList();
        try{
            GenericEntity<ArrayList<Grade>> grades = new GenericEntity<ArrayList<Grade>>(oc){};
            return Response.status(200).header("Success","fetched").entity(grades).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response dodajPrzedmiot(Course p){
        if(Model.getInstance().addPrzedmiot(p)){
            try {
                return Response.created(new URI("/courses/"+p.getlId())).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return Response.status(404).build();
            }
        }else{
            return Response.status(404).build();
        }
    }

    @POST
    @Path("/{id}/grades")
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public synchronized Response addGrade(Grade g, @PathParam("id") int id){
        if(Model.getInstance().addGrade(g,id)){
            try {
                return Response.created(new URI("/courses/"+id+"/grades/"+g.getlId())).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return Response.status(404).build();
            }
        }else{
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response aktualizujPrzedmiot(@PathParam("id") int id, Course c){
        org.mongodb.morphia.query.Query<Course> query = Model.getInstance().datastore.createQuery(Course.class).field("lId").equal(id);
        Course co = query.get();
        if(co != null){
            String teacher = null;
            String name = null;
            if(c.getTeacher()!= null){
                teacher = c.getTeacher();
            }else{
                teacher = co.getTeacher();
            }

            if(c.getNazwa() != null){
                name = c.getNazwa();
            }else{
                name = co.getNazwa();
            }
            Model.getInstance().datastore.update(query,
                    Model.getInstance().datastore.createUpdateOperations(Course.class).set("name",name)
                            .set("teacher",teacher));
            return Response.status(200).header("Success","updated").build();
        }else{
            return Response.status(404).build();
        }
    }

    @PUT
    @Path("/{id}/grades/{idO}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response aktualizujGrade(Grade g, @PathParam("id") int id,@PathParam("idO") int idO){
        org.mongodb.morphia.query.Query<Grade> query = Model.getInstance().datastore.createQuery(Grade.class).field("lId").equal(idO);
        Grade gr = query.get();
        if(gr != null && gr.getIdPrzedmiot() == id){
            Integer student = null;
            Double grade = null;
            Date date = null;

            if(g.getIdStudent()!= null){
                student = g.getIdStudent();
            }else{
                student = gr.getIdStudent();
            }

            if(g.getStopien() != null){
                grade = g.getStopien();
            }else{
                grade = gr.getStopien();
            }

            if(g.getDataWystawienia()!= null){
                date = g.getDataWystawienia();
            }else{
                date = gr.getDataWystawienia();
            }

            Model.getInstance().datastore.update(query,
                    Model.getInstance().datastore.createUpdateOperations(Grade.class).set("course",id)
                            .set("student",student)
                            .set("grade",grade)
                            .set("date",date));
            return Response.status(201).header("Success","updated").build();
        }else{
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public synchronized Response deleteCourse(@PathParam("id") int i){
        org.mongodb.morphia.query.Query<Course> query = Model.getInstance().datastore.createQuery(Course.class).field("lId").equal(i);
        Course co = query.get();
        if(co != null) {
            Model.getInstance().datastore.delete(query);
            org.mongodb.morphia.query.Query<Grade> query1 = Model.getInstance().datastore.createQuery(Grade.class).field("course").equal(i);
            Model.getInstance().datastore.delete(query1);
            return Response.status(204).header("Success","deleted").build();
        }else{
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("/{id}/grades/{idGrade}")
    public synchronized Response deleteGrade(@PathParam("id") int i,@PathParam("idGrade") int idGrade){
        org.mongodb.morphia.query.Query<Grade> query = Model.getInstance().datastore.createQuery(Grade.class).field("lId").equal(idGrade);
        Grade gr = query.get();
        if(/*Model.getInstance().getGrades().get(gr.getId()) != null &&*/ gr != null) {
            Model.getInstance().datastore.delete(query);
            /*Model.getInstance().getGrades().remove(gr.getId());*/
            return Response.status(204).header("Success","deleted").build();
        }else{
            return Response.status(404).build();
        }
    }

}
