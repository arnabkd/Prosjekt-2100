        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        movl    $10,%eax                # 10
        movl    %eax,-4(%ebp)           # LF =
        call    getchar                 # Call getchar
        call    getint                  # Call getint
        movl    -4(%ebp),%eax           # LF
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
        movl    $10,%eax                # 10
        pushl   %eax                    # Push parameter #1
        call    putint                  # Call putint
        popl    %ecx                    # Pop parameter #1
        movl    $0,%eax                 # 0
        pushl   %eax                    # Push parameter #1
        call    exit                    # Call exit
        popl    %ecx                    # Pop parameter #1
.exit$main:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
