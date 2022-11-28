package com.project.LMSSU.Controller;

import com.project.LMSSU.DTO.IncompleteContentsResponseDTO;
import com.project.LMSSU.Service.IncompleteContentsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/incomplete")
@Tag(name = "Incomplete Controller", description = "마감 임박 강의/과제 가져오기 Controller")
public class IncompleteContentsController {
    private final IncompleteContentsService incompleteContentsService;

    @GetMapping()
    @Operation(summary = "마감 임박 강의/과제를 가져온다.", description = "마감 3일 이내의 강의/과제")
    public Map getIncompleteContent(Long studentId){
        Map map = new HashMap();
        List<IncompleteContentsResponseDTO> list = incompleteContentsService.getIncompleteContents(studentId);
        map.put("Results", list);

        return map;
    }

}
