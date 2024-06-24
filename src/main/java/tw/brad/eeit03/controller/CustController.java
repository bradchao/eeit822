package tw.brad.eeit03.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tw.brad.eeit03.model.CustRepository;
import tw.brad.eeit03.model.MyCust;

@RestController
public class CustController {
    @Autowired
    private CustRepository repository;

    @PostMapping("/mycust")
    public Boolean add(@RequestBody MyCust cust){
        repository.save(cust);
        return true;
    }

    @PutMapping("/mycust/{id}")
    public MyCust update(@PathVariable Integer id,
                         @RequestBody MyCust myCust){
        MyCust cust = repository.findById(id).orElse(null);
        if (cust != null){
            myCust.setId(id);
            repository.save(myCust);
        }
        return repository.findById(id).orElse(null);
    }

    @DeleteMapping("/mycust/{id}")
    public Boolean del(@PathVariable Integer id){
        repository.deleteById(id);
        return true;
    }

    @GetMapping("/mycust/{id}")
    public MyCust query(@PathVariable Integer id){
        MyCust cust = repository.findById(id).orElse(null);
        return cust;
    }

}
