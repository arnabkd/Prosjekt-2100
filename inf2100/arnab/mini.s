        .data
        .globl  k
k:      .fill   4                       # int k;
        .text
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        movl    $120,%eax               # 120
        pushl   %eax                    # Push parameter #1
        call    putchar                 # Call putchar
        popl    %ecx                    # Pop parameter #1
.exit$main:                                
        popl    %ebp                    
        ret                             # End function main
