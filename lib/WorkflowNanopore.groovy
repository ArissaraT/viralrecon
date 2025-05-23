//
// This file holds several functions specific to the workflow/nanopore.nf in the nf-core/viralrecon pipeline
//

class WorkflowNanopore {

    //
    // Check and validate parameters
    //
    public static void initialise(params, log, valid_params) {
        WorkflowCommons.genomeExistsError(params, log)

        // Generic parameter validation
        if (!params.fasta) {
            log.error "Genome fasta file not specified with e.g. '--fasta genome.fa' or via a detectable config file."
            System.exit(1)
        }

        if (!params.primer_bed) {
            log.error "Primer BED file not specified with e.g. '--primer_bed primers.bed' or via a detectable config file."
            System.exit(1)
        }

        // if (!params.artic_scheme) {
        //     log.error "ARTIC scheme not specified with e.g. --artic_scheme 'nCoV-2019' or via a detectable config file."
        //     System.exit(1)
        // }

        // if (!valid_params['artic_minion_caller'].contains(params.artic_minion_caller)) {
        //     log.error "Invalid option: ${params.artic_minion_caller}. Valid options for '--artic_minion_caller': ${valid_params['artic_minion_caller'].join(', ')}."
        //     System.exit(1)
        // }

        // if (!valid_params['artic_minion_aligner'].contains(params.artic_minion_aligner)) {
        //     log.error "Invalid option: ${params.artic_minion_aligner}. Valid options for '--artic_minion_aligner': ${valid_params['artic_minion_aligner'].join(', ')}."
        //     System.exit(1)
        // }

        if (!params.fastq_dir) {
            log.error "Please specify a valid folder containing ONT basecalled fastq files generated by guppy_barcoder or guppy_basecaller e.g. '--fastq_dir ./20191023_1522_MC-110615_0_FAO93606_12bf9b4f/fastq_pass/"
            System.exit(1)
        }

        // if (params.artic_minion_caller == 'nanopolish') {
        //     if (!params.fast5_dir) {
        //         log.error "Please specify a valid folder containing ONT fast5 files e.g. '--fast5_dir ./20191023_1522_MC-110615_0_FAO93606_12bf9b4f/fast5_pass/"
        //         System.exit(1)
        //     }
        //     if (!params.sequencing_summary) {
        //         log.error "Please specify a valid ONT sequencing summary file e.g. '--sequencing_summary ./20191023_1522_MC-110615_0_FAO93606_12bf9b4f/sequencing_summary.txt"
        //         System.exit(1)
        //     }
        // }

        // if (params.artic_minion_caller == 'medaka') {
        //     if (!params.artic_minion_medaka_model) {
        //         log.error "Please specify the '--artic_minion_medaka_model' parameter too if using the '--artic_minion_caller medaka' workflow.\nSee https://github.com/nanoporetech/medaka"
        //         System.exit(1)
        //     }
        // }
    }
}
