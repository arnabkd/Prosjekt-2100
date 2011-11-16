        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $8,%esp                 # Get 8 bytes local data space
        movl    $2,%eax                 # 2
        movl    %eax,-4(%ebp)           # b =
        movl    -4(%ebp),%eax           # b
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-8(%ebp)           # c =
.exit$main:
        addl    $8,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
