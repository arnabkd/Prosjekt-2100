        .globl  one                     
one:    pushl   %ebp                    # Start function one
        movl    %esp,%ebp               
        movl    $1,%eax                 # 1
        jmp     .exit$one               # return-statement
.exit$one:                                
        popl    %ebp                    
        ret                             # End function one
        .globl  main                    
main:   pushl   %ebp                    # Start function main
        movl    %esp,%ebp               
        subl    $4,%esp                 # Get 4 bytes local data space
        call    one                     # Call one
        pushl   %eax                    
        call    one                     # Call one
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        call    one                     # Call one
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    $1,%eax                 # 1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    -4(%ebp),%eax           # k
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    $2,%eax                 # 2
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        pushl   %eax                    
        movl    -4(%ebp),%eax           # k
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               # Compute +
        movl    %eax,-4(%ebp)           # k =
.exit$main:
        addl    $4,%esp                 # Release local data space
        popl    %ebp                    
        ret                             # End function main
