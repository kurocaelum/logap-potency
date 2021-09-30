package com.logap.potencycurve.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.logap.potencycurve.entities.Curve;
import com.logap.potencycurve.entities.Pair;
import com.logap.potencycurve.services.CurveService;
import com.logap.potencycurve.services.PairService;

@RestController
@RequestMapping(value = "/curve")
public class CurveController {

	@Autowired
	private CurveService curveService;
	
	@Autowired
	private PairService pairService;
	
	@GetMapping
	public ResponseEntity<List<Curve>> findAll() {
		List<Curve> list = curveService.findAll();
		return ResponseEntity.ok(list);
	}
	
	@PostMapping
	public ResponseEntity<Void> insert(@RequestParam String name, @RequestParam("file") MultipartFile file) {
		Curve curve = new Curve();
		curve.setName(name);
		curve.setPlotData(pairService.csvToPairs(file, curve));
		
		curveService.save(curve);
		pairService.saveAll(curve.getPlotData());
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(curve.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	//TODO TESTE
	@GetMapping(value = "/test")
	public ResponseEntity<List<String[]>> readCsv() {
		List<String[]> csv = curveService.readCsv();
		return ResponseEntity.ok(csv);
	}
	
	//TODO TESTE
	@GetMapping(value = "/test1")
	public ResponseEntity<List<Pair>> csvToPairs() {
		List<Pair> pairs = curveService.csvToPairsTest();
		return ResponseEntity.ok(pairs);
	}
}
