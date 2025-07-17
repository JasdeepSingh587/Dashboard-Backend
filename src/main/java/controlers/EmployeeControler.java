package controlers;

import dto.EmployessDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="/employees")
public class EmployeeControler {

    @GetMapping("{employeeId}")
    public EmployessDTO getMessage(@PathVariable Long employeeId){
//        return "This is a message";
        return new EmployessDTO(employeeId,"Anuj","anuj@gmail.com");
    }
}
