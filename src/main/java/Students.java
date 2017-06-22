import project.structures.Grade;
import project.structures.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by student on 26.02.2017.
 */
@Path("/students")
public class Students {

    /*@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudentsHeader(@Context MediaType type) {

        ArrayList<Student> stu= new ArrayList<Student>();

        for(Iterator i = Model.getInstance().getStudents().entrySet().iterator(); i.hasNext();) {
            Map.Entry para = (Map.Entry) i.next();
            Student st = (Student) para.getValue();
            stu.add(st);
        }

        if(stu.size() > 0) {
            GenericEntity<ArrayList<Student>> students = new GenericEntity<ArrayList<Student>>(stu){};
                return Response.status(200).header("Success", "fetched").entity(students).build();
        }else{
            return Response.status(404).build();
        }
    }*/

    /*@GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudentsHeader(@Context MediaType type) {
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore.createQuery(Student.class);
        ArrayList<Student> stu = (ArrayList<Student>) query.asList();
        if(stu.size() > 0) {
            GenericEntity<ArrayList<Student>> students = new GenericEntity<ArrayList<Student>>(stu){};
            return Response.status(200).header("Success", "fetched").entity(students).build();
        }else{
            return Response.status(404).build();
        }
    }*/



    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response getAllStudents(@QueryParam("fname") String fname, @QueryParam("lname") String lname) {
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore
                .createQuery(Student.class);
        if(fname != null) {
            query.field("firstname").equal(fname);
        }

        if(lname != null) {
            query.field("lastname").equal(lname);
        }
        ArrayList<Student> students = new ArrayList<Student>();
        students = (ArrayList<Student>) query.asList();
        try{
            return Response.ok(new GenericEntity<ArrayList<Student>>(students) {
            }).build();
        } catch (Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/dates")
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response getStudentByDate(@QueryParam("date") String date, @QueryParam("before") String before, @QueryParam("after") String after) {
        SimpleDateFormat form = new SimpleDateFormat("yyyy/MM/dd");
        form.setTimeZone(TimeZone.getTimeZone("CET"));
        Date dat1 = null;
        Date dat2 = null;
        Date dat3 = null;
        if(date != null) {
            try {
                dat1 = form.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(before != null) {
            try {
                dat2 = form.parse(before);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(after != null) {
            try {
                dat3 = form.parse(after);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore
                .createQuery(Student.class);
        if(date != null) {
            query.field("birth_date").equal(dat1);
        }else{
            if(dat2 != null){
                query.field("birth_date").lessThan(dat2);
            }
            if(dat3 != null){
                query.field("birth_date").greaterThan(dat3);
            }
        }

        ArrayList<Student> students = new ArrayList<Student>();
        if(query.asList() != null)students = (ArrayList<Student>) query.asList();
        try{
            return Response.ok(new GenericEntity<ArrayList<Student>>(students) {
            }).build();
        } catch (Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudent(@PathParam("id") int id){
        Student st = new Student();
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore.createQuery(Student.class).field("lId").equal(id);
        if(query.asList() != null)st = query.get();
        try{
            return Response.status(200).header("Success","fetched").entity(st).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/{id}/grades")
    @Consumes(MediaType.APPLICATION_JSON)
    public synchronized Response getStudentGrades(@PathParam("id") int id, @QueryParam("course") Integer course, @QueryParam("greater") Double gGrade,@QueryParam("less") Double lGrade) {
        org.mongodb.morphia.query.Query<Grade> query = Model.getInstance().datastore.createQuery(Grade.class).field("student").equal(id);
        if(course != null && course>=0) {
            query.field("course").equal(course);
        }

        if(gGrade != null) {
            query.field("grade").greaterThan(gGrade);
        }

        if(lGrade != null){
            query.field("grade").lessThan(lGrade);
        }

        ArrayList<Grade> oc = new ArrayList<Grade>();
        if(query.asList() != null)oc = (ArrayList<Grade>) query.asList();
        try{
            GenericEntity<ArrayList<Grade>> grades = new GenericEntity<ArrayList<Grade>>(oc){};
            return Response.status(200).header("Success","fetched").entity(grades).build();
        }catch (Exception e){
            return Response.status(404).build();
        }
    }

    /*@GET
    @Path("/{id}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudentGrades(@PathParam("id") int id){
        org.mongodb.morphia.query.Query<Grade> query = Model.getInstance().datastore.createQuery(Grade.class).field("student").equal(id);
        ArrayList<Grade> oc = (ArrayList<Grade>) query.asList();
        if(oc.size() > 0) {
            GenericEntity<ArrayList<Grade>> grades = new GenericEntity<ArrayList<Grade>>(oc){};
            return Response.status(200).header("Success","fetched").entity(grades).build();
        }else{
            return Response.status(404).build();
        }
    }*/

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response dodajStudenta(Student s){
        if(Model.getInstance().addStudent(s)){
            try {
                return Response.created(new URI("/students/"+s.getlId())).build();
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
    public synchronized Response aktualizujStudenta(@PathParam("id") int id, Student s){
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore.createQuery(Student.class).field("lId").equal(id);
        Student st = query.get();
        if(s.getDataUrodzenia()!=null && s.getNazwisko()!=null && s.getImie()!=null) {
            if (st != null) {

                Model.getInstance().datastore.update(query,
                        Model.getInstance().datastore.createUpdateOperations(Student.class).set("firstname", s.getImie())
                                .set("lastname", s.getNazwisko())
                                .set("birth_date", s.getDataUrodzenia()));

                return Response.status(200).header("Success", "updated").build();
            } else {
                return Response.status(404).build();
            }
        }else{
            return Response.status(400).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public synchronized Response deleteStudent(@PathParam("id") int i){
        org.mongodb.morphia.query.Query<Student> query = Model.getInstance().datastore.createQuery(Student.class).field("lId").equal(i);
        Student st = query.get();
        if(st != null) {
            Model.getInstance().datastore.delete(query);
            org.mongodb.morphia.query.Query<Grade> queryOc = Model.getInstance().datastore.createQuery(Grade.class).field("student").equal(i);
            Model.getInstance().datastore.delete(queryOc);
            return Response.status(204).header("Success","deleted").build();
        }else{
            return Response.status(404).build();
        }
    }

    /*@GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudent(@PathParam("id") int id){
        Student st = Model.getInstance().getStudents().get(id);
        if(st != null){
            return Response.status(200).header("Success","fetched").entity(st).build();
        }else{
            return Response.status(404).build();
        }
    }

    @GET
    @Path("/{id}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getStudentGrades(@PathParam("id") int id){
        ArrayList<Grade> oc= new ArrayList<Grade>();
        HashMap<ObjectId, Grade> oceny = Model.getInstance().getGrades();
        for(Iterator iG = oceny.entrySet().iterator(); iG.hasNext();){
            Map.Entry para = (Map.Entry) iG.next();
            Grade gr = (Grade) para.getValue();
            if(gr.getIdStudent() == id){
                oc.add(gr);
            }
        }
        if(oc.size() > 0) {
            GenericEntity<ArrayList<Grade>> grades = new GenericEntity<ArrayList<Grade>>(oc){};
                return Response.status(200).header("Success","fetched").entity(grades).build();
        }else{
            return Response.status(404).build();
        }
    }

    @PUT
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response aktualizujStudenta(Student s){
        Student st = Model.getInstance().getStudents().get(s.getID());
        if(st != null){
            st.setImie(s.getImie());
            st.setNazwisko(s.getNazwisko());
            st.setDataUrodzenia(s.getDataUrodzenia());
            Model.getInstance().getStudents().put(st.getID(),st);
            return Response.status(201).header("Success","updated").build();
        }else{
            return Response.status(404).build();
        }
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized Response dodajStudenta(Student s){
        if(Model.getInstance().addStudent(s)){
            try {
                return Response.created(new URI("/students/"+s.getIndeks())).build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
                return Response.status(404).build();
            }
        }else{
            return Response.status(404).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public synchronized Response deleteStudent(@PathParam("id") int i){
        if(Model.getInstance().getStudents().get(i) != null) {
            Model.getInstance().getStudents().remove(i);
            for(Iterator iG = Model.getInstance().getGrades().entrySet().iterator(); iG.hasNext();){
                Map.Entry para = (Map.Entry) iG.next();
                Grade gr = (Grade) para.getValue();
                if(gr.getIdStudent() == i){
                    Model.getInstance().getGrades().remove(gr.getId());
                }
            }
            return Response.status(204).header("Success","deleted").build();
        }else{
            return Response.status(404).build();
        }
    }
*/
}
