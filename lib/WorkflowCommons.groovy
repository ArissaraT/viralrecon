//
// This file holds several functions common to the multiple workflows in the nf-core/viralrecon pipeline
//

class WorkflowCommons {

    //
    // Exit pipeline if incorrect --genome key provided
    //
    private static void genomeExistsError(params, log) {
        if (params.genomes && params.genome && !params.genomes.containsKey(params.genome)) {
            log.error "=============================================================================\n" +
                "  Genome '${params.genome}' not found in any config files provided to the pipeline.\n" +
                "  Currently, the available genome keys are:\n" +
                "  ${params.genomes.keySet().join(", ")}\n" +
                "==================================================================================="
            System.exit(1)
        }
    }

    //
    // Get workflow summary for MultiQC
    //
    public static String paramsSummaryMultiqc(workflow, summary) {
        String summary_section = ''
        for (group in summary.keySet()) {
            def group_params = summary.get(group)  // This gets the parameters of that particular group
            if (group_params) {
                summary_section += "    <p style=\"font-size:110%\"><b>$group</b></p>\n"
                summary_section += "    <dl class=\"dl-horizontal\">\n"
                for (param in group_params.keySet()) {
                    summary_section += "        <dt>$param</dt><dd><samp>${group_params.get(param) ?: '<span style=\"color:#999999;\">N/A</a>'}</samp></dd>\n"
                }
                summary_section += "    </dl>\n"
            }
        }

        String yaml_file_text  = "id: '${workflow.manifest.name.replace('/','-')}-summary'\n"
        yaml_file_text        += "description: ' - this information is collected when the pipeline is started.'\n"
        yaml_file_text        += "section_name: '${workflow.manifest.name} Workflow Summary'\n"
        yaml_file_text        += "section_href: 'https://github.com/${workflow.manifest.name}'\n"
        yaml_file_text        += "plot_type: 'html'\n"
        yaml_file_text        += "data: |\n"
        yaml_file_text        += "${summary_section}"
        return yaml_file_text
    }

    //
    // Function to check whether primer BED file has the correct suffixes as provided to the pipeline
    //
    public static void checkPrimerSuffixes(primer_bed_file, primer_left_suffix, primer_right_suffix, log) {
        def total = 0
        def left  = 0
        def right = 0
        primer_bed_file.eachLine { line ->
            total += 1
            def name = line.split('\t')[3]
            if (name.contains(primer_left_suffix)) {
                left += 1
            } else if (name.contains(primer_right_suffix)) (
                right += 1
            )
        }
        if (total != (left + right)) {
            log.warn "=============================================================================\n" +
                "  Please check the name field (column 4) in the file supplied via --primer_bed.\n\n" +
                "  All of the values in that column do not end with those supplied by:\n" +
                "      --primer_left_suffix : $primer_left_suffix\n" +
                "      --primer_right_suffix: $primer_right_suffix\n\n" +
                "  This information is required to collapse the primer intervals into amplicons\n" +
                "  for the coverage plots generated by the pipeline.\n" +
                "==================================================================================="
        }
    }

    //
    // Function to get field entry from Pangolin output file
    //
    // See: https://stackoverflow.com/a/67766919
    public static String getFieldFromPangolinReport(pangolin_report, col_name) {
        def headers = []
        def field   = ''
        pangolin_report.readLines().eachWithIndex { row, row_index ->
            if (row_index == 0) { 
                headers = row.split(',') 
            } else {
                def col_map = [:]
                def cells = row.split(',').eachWithIndex { cell, cell_index ->
                    col_map[headers[cell_index]] = cell
                }
                if (col_map.containsKey(col_name)) {
                    field = col_map[col_name]
                }
            }
        }
        return field
    }

    //
    // Function to get number of variants reported in BCFTools stats file
    //
    public static Integer getNumVariantsFromBCFToolsStats(bcftools_stats) {
        def num_vars = 0
        bcftools_stats.eachLine { line ->
            def matcher = line =~ /SN\s*0\s*number\sof\srecords:\s*([\d]+)/
            if (matcher) num_vars = matcher[0][1].toInteger()
        }
        return num_vars
    }
}
