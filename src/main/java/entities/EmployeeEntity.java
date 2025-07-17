//package entities;
////import jakarta.persistance.Entity;
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//
//
//@EntityScan
//@Table(name="employees")
//public class EmployeeEntity {
//
//    @Id
//    private Long Id;
//    private  String name;
//    private  String email;
//
//    public Long getId() {
//        return Id;
//    }
//
//    public void setId(Long id) {
//        Id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//}


package entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    private Long id; // use lowercase 'id' by convention
    private String name;
    private String email;
    public void setId(Long id) {
        this.id = id;
    }

}
